package com.lambdarat.manual

import com.lambdarat.common.Domain
import com.lambdarat.common.Domain.{Group, GroupEvent, NotifierError, User}
import com.lambdarat.common.services.{GroupsImpl, Notifier, NotifierImpl, UsersImpl}
import org.scalatest.{FlatSpec, Matchers}

class MeetupManualTest extends FlatSpec with Matchers {

  // This notifier does not notify anybody
  private val mockNotifier = new Notifier {

    override def notifyManagerEvent(
      event: Domain.GroupEvent, group: Group
    ): Either[Domain.NotifierError, Seq[User.Id]] = {
      println(s"Not notifying users of group ${group.gid}")

      val userIds = group.users.collect { case User(_, _, Some(id)) => id }

      val mockedResult: Either[NotifierError, Seq[User.Id]] = event match {
        case GroupEvent.UserAdded(uid)    => Right(uid +: userIds)
        case GroupEvent.UserRemoved(uid)  => Right(userIds.filter(_ != uid))
      }

      mockedResult
    }

  }

  private val manager = new ManagerImpl(mockNotifier)

  private val users = new UsersImpl

  private val groups = new GroupsImpl

  private val meetup = new MeetupImpl(users, groups, manager)

  "A meetup" should "remove a user from a group if it exists" in {

    // No user notified in real life
    val removeAttempt = for {
      uid   <- meetup.registerUser(User.Name("Pepe"), User.Age(25))
      gid   <- meetup.registerGroup(Group.Name("ScalaMAD"))
      group <- meetup.joinUserToGroup(uid, gid)
    } yield {

      group.gid shouldBe defined
      group.gid.map(_ shouldBe gid)

      group.users should have size 1
    }

    removeAttempt shouldBe a[Right[_, _]]

  }

}
