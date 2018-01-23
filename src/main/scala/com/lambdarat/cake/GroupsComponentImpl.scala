package com.lambdarat.cake
import com.lambdarat.common.services.{Groups, GroupsImpl}

trait GroupsComponentImpl extends GroupsComponent {

  override val groups: Groups = new GroupsImpl

}
