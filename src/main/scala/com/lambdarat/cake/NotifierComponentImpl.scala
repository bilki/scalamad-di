package com.lambdarat.cake
import com.lambdarat.common.services.{Notifier, NotifierImpl}

trait NotifierComponentImpl extends NotifierComponent {

  override val notifier: Notifier = new NotifierImpl

}
