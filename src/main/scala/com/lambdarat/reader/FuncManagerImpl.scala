package com.lambdarat.reader

import com.lambdarat.common.Domain._

object FuncManagerImpl {

  type NotifyGroup = (GroupEvent, Group) => Either[NotifierError, Seq[User.Id]]

  val addUserToGroup: NotifyGroup => (User, Group)
    => Either[MeetupError, Group] = notifier => (user, group) => {

    lazy val alreadyPresentError = GroupsError(s"User with uid ${user.uid} is already present in group ${group.gid}")

    lazy val userWithoutIdError = GroupsError(s"User ${user.name} came without a proper uid")

    for {
      uid <- user.uid.toRight(userWithoutIdError)
      _   <- Either.cond(!group.users.exists(_.uid == user.uid), (), alreadyPresentError)
      _   <- notifier(GroupEvent.UserAdded(uid), group)
    } yield {
      group.copy(users = user +: group.users)
    }

  }

  val removeUserFromGroup: NotifyGroup => (User.Id, Group)
    => Either[MeetupError, Group] = notifier => (uid, group) => {

    lazy val notPresentError = GroupsError(s"User with uid $uid is not present in group ${group.gid}")

    val userIndex = group.users.indexWhere(_.uid.exists(_ == uid))

    lazy val groupWithoutUser = group.copy(users = group.users.take(userIndex) ++ group.users.drop(userIndex + 1))

    for {
      group <- Either.cond(userIndex != -1, groupWithoutUser, notPresentError)
      _     <- notifier(GroupEvent.UserRemoved(uid), group)
    } yield {
      group
    }

  }

}
