package org.activiti.latera.bss.eventListeners
import org.activiti.engine.delegate.DelegateExecution

import org.activiti.latera.bss.eventListeners.AbstractListener
import org.activiti.engine.delegate.event.*
import org.activiti.engine.delegate.event.impl.*

import org.slf4j.LoggerFactory

public class EventLogging extends AbstractListener {
  def protected execute(DelegateExecution execution, ActivitiEvent event) {

    def logStr = "Occured ${event.getType()} event"

    def detailedLogStr = getDetailedLog(event)
    if (detailedLogStr) {
      logStr = "${logStr}: ${detailedLogStr}"
    }

    log(logStr, 'debug', execution)
  }

  def private getDetailedLog(ActivitiEvent event) {
    def detailedLog = null
    
    switch (event) {
      case ActivitiActivityEventImpl:
        detailedLog = "${event.getActivityId()} (${event.getActivityType()} - ${event.getActivityName()})"
        break
      case ActivitiEntityEvent:
        detailedLog = event.getEntity().getClass().name
        break
      case ActivitiVariableEvent:
        detailedLog = "${event.getVariableName()} = ${event.getVariableValue()} (${event.getVariableType()})"
        break
    }
    
    detailedLog
  }

  def public boolean isFailOnException() {
    false
  }
}
