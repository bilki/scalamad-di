package com.lambdarat.naive

import com.lambdarat.common.Domain.{Group, NotifierError, User}

trait Notifier {

  def notifyManagerEvent(event: Manager.Event, group: Group): Either[NotifierError, Seq[User.Id]]

}
