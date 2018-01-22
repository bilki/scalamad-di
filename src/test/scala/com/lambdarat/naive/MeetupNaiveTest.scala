package com.lambdarat.naive

import com.lambdarat.common.Domain.{Group, User}
import org.scalatest.{FlatSpec, Matchers}

class MeetupNaiveTest extends FlatSpec with Matchers {

  private val meetup = new MeetupImpl

  "A meetup" should "add a user to a group if both exist" in {

    // Notifying users!
    val joinAttempt = for {
      uid   <- meetup.registerUser(User.Name("Pepe"), User.Age(25))
      uid2  <- meetup.registerUser(User.Name("Antonio"), User.Age(30))
      gid   <- meetup.registerGroup(Group.Name("ScalaMAD"))
      _     <- meetup.joinUserToGroup(uid, gid)
      group <- meetup.joinUserToGroup(uid2, gid)
    } yield {

      group.gid shouldBe Some(gid)

      group.users should have size 2

    }

    joinAttempt shouldBe a[Right[_, _]]

  }

}
