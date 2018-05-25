package org.activiti.latera.bss.executionListeners

import org.activiti.engine.delegate.ExecutionListener
import org.activiti.engine.delegate.DelegateExecution

class AbstractListener implements ExecutionListener {

  void notify(DelegateExecution execution) {
    // do stuff
  }
}
