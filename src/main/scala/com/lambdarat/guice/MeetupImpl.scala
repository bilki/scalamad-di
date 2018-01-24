package com.lambdarat.guice

import javax.inject.{Inject, Singleton}

import com.lambdarat.common.Domain.{Group, MeetupError, User}
import com.lambdarat.common.services._

@Singleton
class MeetupImpl @Inject()(users: Users, groups: Groups, manager: Manager) extends Meetup {

  override def registerUser(name: User.Name, age: User.Age): Either[MeetupError, User.Id] = {

    val user = User(name, age)

    users.createUser(user)

  }

  override def deleteUser(uid: User.Id): Either[MeetupError, User] = {

    users.deleteUser(uid)

  }

  override def registerGroup(name: Group.Name): Either[MeetupError, Group.Id] = {

    val group = Group(name, Seq.empty)

    groups.saveGroup(group)

  }

  override def closeGroup(gid: Group.Id): Either[MeetupError, Group] = {

    groups.closeGroup(gid)

  }

  override def getGroupUsers(gid: Group.Id): Either[MeetupError, Seq[User]] = {

    for {
      group <- groups.getGroup(gid)
    } yield {
      group.users
    }

  }

  override def joinUserToGroup(uid: User.Id, gid: Group.Id): Either[MeetupError, Group] = {

    for {
      user          <- users.getUser(uid)
      group         <- groups.getGroup(gid)
      groupWithUser <- manager.addUserToGroup(user, group)
      _             <- groups.saveGroup(groupWithUser)
    } yield {
      groupWithUser
    }

  }

  override def removeUserFromGroup(uid: User.Id, gid: Group.Id): Either[MeetupError, Group] = {

    for {
      _           <- users.getUser(uid)
      group       <- groups.getGroup(gid)
      groupWoUser <- manager.removeUserFromGroup(uid, group)
      _           <- groups.saveGroup(group)
    } yield {
      groupWoUser
    }

  }

}
