package com.lambdarat.functional

import com.lambdarat.common.Domain._
import com.lambdarat.common.services.Notifier
import com.lambdarat.manual.ManagerImpl

object FuncManagerImpl {

  val addUserToGroup: Notifier => (User, Group) => Either[MeetupError, Group] = notifier => (user, group) => {

    new ManagerImpl(notifier).addUserToGroup(user, group)

  }

  val removeUserFromGroup: Notifier => (User.Id, Group) => Either[MeetupError, Group] = notifier => (uid, group) => {

    new ManagerImpl(notifier).removeUserFromGroup(uid, group)

  }

}
