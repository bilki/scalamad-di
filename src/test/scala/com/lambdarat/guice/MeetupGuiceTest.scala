package com.lambdarat.guice

import com.google.inject.util.Modules
import com.google.inject.{AbstractModule, Guice}
import com.lambdarat.common.Domain.{Group, GroupEvent, NotifierError, User}
import com.lambdarat.common.services.Notifier
import org.scalatest.{FlatSpec, Matchers}

class MeetupGuiceTest extends FlatSpec with Matchers {

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

  object TestModule extends AbstractModule {
    override def configure(): Unit = {
      bind(classOf[Notifier]).toInstance(mockNotifier)
    }
  }

  private val guice = Guice.createInjector(Modules.`override`(MeetupModule).`with`(TestModule))

  private val meetup = guice.getInstance(classOf[Meetup])

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
