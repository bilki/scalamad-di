package com.lambdarat.reader

import com.lambdarat.common.Domain._

object FuncMeetupImpl {

  // Some types are identical, improve
  type GetUser = User.Id => Either[UsersError, User]
  type CreateUser = User => Either[MeetupError, User.Id]
  type DeleteUser = User.Id => Either[MeetupError, User]
  type GetGroup = Group.Id => Either[GroupsError, Group]
  type SaveGroup = Group => Either[MeetupError, Group.Id]
  type CloseGroup = Group.Id => Either[MeetupError, Group]
  type JoinUserToGroup = (User, Group) => Either[MeetupError, Group]
  type RemoveUserFromGroup = (User.Id, Group) => Either[MeetupError, Group]

  val registerUser: CreateUser => (User.Name, User.Age) => Either[MeetupError, User.Id] = createUser => (name, age) => {

    val user = User(name, age)

    createUser(user)

  }

  val deleteUser: DeleteUser => User.Id => Either[MeetupError, User] =  deleteUser => uid => {

    deleteUser(uid)

  }

  val registerGroup: SaveGroup => Group.Name => Either[MeetupError, Group.Id] = saveGroup => name => {

    val group = Group(name, Seq.empty)

    saveGroup(group)

  }

  val closeGroup: CloseGroup => Group.Id => Either[MeetupError, Group] = closeGroup => gid => {

    closeGroup(gid)

  }

  val getGroupUsers: GetGroup => Group.Id => Either[MeetupError, Seq[User]] = getGroup => gid => {

    for {
      group <- getGroup(gid)
    } yield {
      group.users
    }

  }

  val joinUserToGroup: (GetUser, GetGroup, SaveGroup, JoinUserToGroup) => (User.Id, Group.Id) => Either[MeetupError, Group] =
    (getUser, getGroup, saveGroup, joinUserToGroup) => (uid, gid) => {

    for {
      user          <- getUser(uid)
      group         <- getGroup(gid)
      groupWithUser <- joinUserToGroup(user, group)
      _             <- saveGroup(groupWithUser)
    } yield {
      groupWithUser
    }

  }

  val removeUserFromGroup: (GetUser, GetGroup, SaveGroup, RemoveUserFromGroup) => (User.Id, Group.Id) => Either[MeetupError, Group] =
    (getUser, getGroup, saveGroup, removeUserFromGroup) => (uid, gid) => {

    for {
      _           <- getUser(uid)
      group       <- getGroup(gid)
      groupWoUser <- removeUserFromGroup(uid, group)
      _           <- saveGroup(group)
    } yield {
      groupWoUser
    }

  }

}
