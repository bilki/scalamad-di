package com.lambdarat.functional

import com.lambdarat.common.Domain.{Group, GroupEvent, NotifierError, User}
import com.lambdarat.common.services.{GroupsImpl, Notifier, UsersImpl}
import org.scalatest.{FlatSpec, Matchers}

class MeetupFunctionalTest extends FlatSpec with Matchers {

  // This notifier does not notify anybody
  private val mockNotifier = new Notifier {

    override def notifyManagerEvent(event: GroupEvent, group: Group): Either[NotifierError, Seq[User.Id]] = {
      println(s"Not notifying users of group ${group.gid}")

      val userIds = group.users.collect { case User(_, _, Some(id)) => id }

      val mockedResult: Either[NotifierError, Seq[User.Id]] = event match {
        case GroupEvent.UserAdded(uid)    => Right(uid +: userIds)
        case GroupEvent.UserRemoved(uid)  => Right(userIds.filter(_ != uid))
      }

      mockedResult
    }

  }

  private val users = new UsersImpl

  private val groups = new GroupsImpl

  private val joinManager = FuncManagerImpl.addUserToGroup(mockNotifier.notifyManagerEvent)

  private val userRegister = FuncMeetupImpl.registerUser(users.createUser)

  private val groupRegister = FuncMeetupImpl.registerGroup(groups.saveGroup)

  private val joinMeetup = FuncMeetupImpl.joinUserToGroup(users.getUser, groups.getGroup, groups.saveGroup, joinManager)

  "A meetup" should "add a user to a group if both exist" in {

    val joinAttempt = for {
      uid   <- userRegister(User.Name("Pepe"), User.Age(25))
      gid   <- groupRegister(Group.Name("ScalaMAD"))
      group <- joinMeetup(uid, gid)  // No user notified in real life
    } yield {

      group.gid shouldBe Some(gid)

      group.users should have size 1
    }

    joinAttempt shouldBe a[Right[_, _]]

  }

}
