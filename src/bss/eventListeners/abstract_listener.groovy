package org.activiti.latera.bss.eventListeners

import org.activiti.engine.delegate.event.*
import org.activiti.engine.delegate.DelegateExecution

public class AbstractListener implements ActivitiEventListener {
  ActivitiEvent event
  DelegateExecution execution
  
  def public void onEvent(ActivitiEvent event) {
    this.event = event
    this.execution = getExecution()
    
    execute()
  }

  def protected execute() {
    // do stuff
  }

  def protected getExecution() {
    def execution = null
    
    def executionId = event.getExecutionId()
    if (executionId) {
      execution = event.getEngineServices().getRuntimeService().createExecutionQuery().executionId(executionId).singleResult()
    }
    
    execution
  }
  
  def protected log(String msg, String level = "info") {
    def logger = getLogger()
    
    if (logger) {
      logger."${level}"(msg)
    } else {
      println msg
    }
  }

  def protected getLogger() {
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
