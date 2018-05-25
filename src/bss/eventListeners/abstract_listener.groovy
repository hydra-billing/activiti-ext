package org.activiti.latera.bss.eventListeners

import org.activiti.engine.delegate.event.ActivitiEventListener
import org.activiti.engine.delegate.event.ActivitiEvent

class AbstractListener implements ActivitiEventListener {

  void onEvent(ActivitiEvent event) {
    // Do stuff
  }

  static protected getExecution(ActivitiEvent event) {
    def execution = null

    def executionId = event.getExecutionId()
    if (executionId) {
      execution = event.getEngineServices().getRuntimeService().createExecutionQuery().executionId(executionId).singleResult()
    }
    execution
  }

  boolean isFailOnException() {
    false
  }
}
