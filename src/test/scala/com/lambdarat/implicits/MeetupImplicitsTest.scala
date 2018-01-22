package com.lambdarat.implicits

import com.lambdarat.common.Domain.{Group, GroupEvent, NotifierError, User}
import com.lambdarat.common.services.{GroupsImpl, Notifier, UsersImpl}
import org.scalatest.{FlatSpec, Matchers}

class MeetupImplicitsTest extends FlatSpec with Matchers {

  // This notifier does not notify anybody
  private implicit val mockNotifier = new Notifier {

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

  private implicit val manager = ManagerImpl

  private implicit val users = new UsersImpl

  private implicit val groups = new GroupsImpl

  private val meetup = MeetupImpl

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
