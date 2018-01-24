package com.lambdarat.cake

import com.lambdarat.common.Domain.{Group, GroupEvent, NotifierError, User}
import com.lambdarat.common.services.{GroupsImpl, Notifier, UsersImpl}
import org.scalatest.{FlatSpec, Matchers}

class MeetupCakeTest extends FlatSpec with Matchers {

  // This notifier does not notify anybody
  trait NotifierComponentMockImpl extends NotifierComponent {
    override val notifier: Notifier = (event: GroupEvent, group: Group) => {
        println(s"Not notifying users of group ${group.gid}")

        val userIds = group.users.collect { case User(_, _, Some(id)) => id }

        val mockedResult: Either[NotifierError, Seq[User.Id]] = event match {
          case GroupEvent.UserAdded(uid)    => Right(uid +: userIds)
          case GroupEvent.UserRemoved(uid)  => Right(userIds.filter(_ != uid))
        }

        mockedResult
      }
  }

  val meetup = new MeetupImpl
    with UsersComponentImpl
    with GroupsComponentImpl
    with NotifierComponentMockImpl
    with ManagerComponentImpl

  "A meetup" should "add a user to a group if both exist" in {

    val joinAttempt = for {
      uid   <- meetup.registerUser(User.Name("Pepe"), User.Age(25))
      gid   <- meetup.registerGroup(Group.Name("ScalaMAD"))
      group <- meetup.joinUserToGroup(uid, gid)  // No user notified in real life
    } yield {

      group.gid shouldBe Some(gid)

      group.users should have size 1
    }

    joinAttempt shouldBe a[Right[_, _]]

  }

}
