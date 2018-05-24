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
        detailedLog = getActivityLog(event)
        break
      case ActivitiEntityEvent:
        detailedLog = getEntityLog(event)
        break
      case ActivitiVariableEvent:
        detailedLog = getVariableLog(event)
        break
    }
    
    detailedLog
  }

  def private getActivityLog(ActivitiEvent event) {
    "${event.getActivityId()} (${event.getActivityType()} - ${event.getActivityName()})"
  }

  def private getEntityLog(ActivitiEvent event) {
    event.getEntity().getClass().name
  }

  def private getVariableLog(ActivitiEvent event) {
    "${event.getVariableName()} = ${event.getVariableValue()} (${event.getVariableType()})"
  }

  def public boolean isFailOnException() {
    false
  }
}
