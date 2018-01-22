package com.lambdarat.naive

import com.lambdarat.common.Domain.{Group, User}
import org.scalatest.{FlatSpec, Matchers}

class MeetupTest extends FlatSpec with Matchers {

  private val meetup = new MeetupImpl

  "A meetup" should "remove a user from a group if it exists" in {

    // Notifying users!
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
