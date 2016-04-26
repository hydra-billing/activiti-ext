package org.activiti.latera.bss.eventListeners

import org.activiti.latera.bss.eventListeners.AbstractListener
import org.activiti.engine.delegate.event.*
import org.activiti.engine.delegate.event.impl.*

public class EventLogging extends AbstractListener {
  def protected execute() {
    def logStr = "Occured ${event.getType()} event"

    def detailedLogStr = getDetailedLog()
    if (detailedLogStr) {
      logStr = "${logStr}: ${detailedLogStr}"
    }

    log logStr
  }

  def private getDetailedLog() {
    def detailedLog = null
    
    switch (event) {
      case ActivitiActivityEventImpl:
        detailedLog = getActivityLog()
        break
      case ActivitiEntityEvent:
        detailedLog = getEntityLog()
        break
      case ActivitiVariableEvent:
        detailedLog = getVariableLog()
        break
    }
    
    detailedLog
  }
  
  def private getActivityLog() {
    "${event.getActivityId()} (${event.getActivityType()} - ${event.getActivityName()})"
  }

  def private getEntityLog() {
    event.getEntity().getClass().name
  }
  
  def private getVariableLog() {
    "${event.getVariableName()} = ${event.getVariableValue()} (${event.getVariableType()})"
  }

  def public boolean isFailOnException() {
    false
  }
}
