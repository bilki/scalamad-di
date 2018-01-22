package com.lambdarat.implicits

import com.lambdarat.common.Domain.{Group, MeetupError, User}
import com.lambdarat.common.services.Notifier

trait Manager {

  def addUserToGroup(user: User, group: Group)(implicit notifier: Notifier): Either[MeetupError, Group]

  def removeUserFromGroup(uid: User.Id, group: Group)(implicit notifier: Notifier): Either[MeetupError, Group]

}
