package com.lambdarat.common.services

import com.lambdarat.common.Domain.{Group, GroupEvent, NotifierError, User}

trait Notifier {

  def notifyManagerEvent(event: GroupEvent, group: Group): Either[NotifierError, Seq[User.Id]]

}
