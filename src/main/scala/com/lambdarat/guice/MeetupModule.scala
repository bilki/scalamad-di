package com.lambdarat.guice

import com.google.inject.AbstractModule
import com.lambdarat.common.services._

object MeetupModule extends AbstractModule {

  override def configure(): Unit = {

    bind(classOf[Users]).to(classOf[UsersImpl]).asEagerSingleton()
    bind(classOf[Groups]).to(classOf[GroupsImpl]).asEagerSingleton()
    bind(classOf[Notifier]).to(classOf[NotifierImpl]).asEagerSingleton()

    bind(classOf[Manager]).to(classOf[ManagerImpl])

    bind(classOf[Meetup]).to(classOf[MeetupImpl])

  }

}
