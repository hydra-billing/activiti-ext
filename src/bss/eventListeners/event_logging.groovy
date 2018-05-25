package org.activiti.latera.bss.eventListeners

import org.activiti.engine.delegate.event.ActivitiEvent
import org.activiti.engine.delegate.event.impl.ActivitiActivityEventImpl
import org.activiti.engine.delegate.event.ActivitiEntityEvent
import org.activiti.engine.delegate.event.ActivitiVariableEvent
import org.activiti.latera.bss.logging.Logging

class EventLogging extends AbstractListener {

  void onEvent(ActivitiEvent event) {
    def logStr = "Occurred ${event.getType()} event"
    def detailedLogStr = getDetailedLog(event)
    if (detailedLogStr) {
      logStr = "${logStr}: ${detailedLogStr}"
    }
    Logging.log(logStr, 'info', Logging.getLogger(event))
  }

  static private getDetailedLog(ActivitiEvent event) {
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

  static private getActivityLog(ActivitiEvent event) {
    "${event.getActivityId()} (${event.getActivityType()} - ${event.getActivityName()})"
  }

  static private getEntityLog(ActivitiEvent event) {
    event.getEntity().getClass().name
  }

  static private getVariableLog(ActivitiEvent event) {
    "${event.getVariableName()} = ${event.getVariableValue()} (${event.getVariableType()})"
  }
}
