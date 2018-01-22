package com.lambdarat.manual

import com.lambdarat.common.Domain.{Group, MeetupError, User}

trait Manager {

  def addUserToGroup(user: User, group: Group): Either[MeetupError, Group]

  def removeUserFromGroup(uid: User.Id, group: Group): Either[MeetupError, Group]

}
