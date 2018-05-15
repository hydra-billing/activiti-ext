package org.activiti.latera.bss.eventListeners

import org.activiti.engine.delegate.event.*
import org.activiti.engine.delegate.DelegateExecution

public class AbstractListener implements ActivitiEventListener {

  def public void onEvent(ActivitiEvent event) {
    def execution = getExecution(event)

    execute(execution, event)
  }

  def protected execute(DelegateExecution execution, ActivitiEvent event) {
    // do stuff
  }

  def protected getExecution(ActivitiEvent event) {
    def execution = null

    def executionId = event.getExecutionId()
    if (executionId) {
      execution = event.getEngineServices().getRuntimeService().createExecutionQuery().executionId(executionId).singleResult()
    }
    execution
  }

  def protected log(String msg, String level = "info", DelegateExecution execution) {
    def logger = getLogger(execution)

    if (logger) {
      logger."${level}"(msg)
    } else {
      println msg
    }
  }

  def protected getLogger(DelegateExecution execution) {
    def logger = null

    if (execution) {
      logger = execution.getVariable("logger")
    }

    logger
  }

  def public boolean isFailOnException() {
    false
  }
}

