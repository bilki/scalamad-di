package com.lambdarat.functional

import com.lambdarat.common.Domain.{Group, MeetupError, User}
import com.lambdarat.common.services._

object FuncMeetupImpl {

  val registerUser: Users => (User.Name, User.Age) => Either[MeetupError, User.Id] = users => (name, age) => {

    val user = User(name, age)

    users.createUser(user)

  }

  val deleteUser: Users => User.Id => Either[MeetupError, User] =  users => uid => {

    users.deleteUser(uid)

  }

  val registerGroup: Groups => Group.Name => Either[MeetupError, Group.Id] = groups => name => {

    val group = Group(name, Seq.empty)

    groups.saveGroup(group)

  }

  val closeGroup: Groups => Group.Id => Either[MeetupError, Group] = groups => gid => {

    groups.closeGroup(gid)

  }

  val getGroupUsers: Groups => Group.Id => Either[MeetupError, Seq[User]] = groups => gid => {

    for {
      group <- groups.getGroup(gid)
    } yield {
      group.users
    }

  }

  val joinUserToGroup: (Users, Groups, (User, Group) => Either[MeetupError, Group]) => (User.Id, Group.Id) => Either[MeetupError, Group] =
    (users, groups, manager) => (uid, gid) => {

    for {
      user          <- users.getUser(uid)
      group         <- groups.getGroup(gid)
      groupWithUser <- manager(user, group)
      _             <- groups.saveGroup(groupWithUser)
    } yield {
      groupWithUser
    }

  }

  val removeUserFromGroup: (Users, Groups, (User.Id, Group) => Either[MeetupError, Group]) => (User.Id, Group.Id) => Either[MeetupError, Group] =
    (users, groups, manager) => (uid, gid) => {

    for {
      _           <- users.getUser(uid)
      group       <- groups.getGroup(gid)
      groupWoUser <- manager(uid, group)
      _           <- groups.saveGroup(group)
    } yield {
      groupWoUser
    }

  }

}
