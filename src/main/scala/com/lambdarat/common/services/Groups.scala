package com.lambdarat.common.services

import com.lambdarat.common.Domain.{Group, GroupsError}

trait Groups {

  def saveGroup(group: Group): Either[GroupsError, Group.Id]

  def getGroup(gid: Group.Id): Either[GroupsError, Group]

  def closeGroup(gid: Group.Id): Either[GroupsError, Group]

}
