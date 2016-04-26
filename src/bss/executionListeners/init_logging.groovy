package org.activiti.latera.bss.executionListeners

import org.activiti.latera.bss.executionListeners.AbstractListener
import org.slf4j.LoggerFactory

public class InitLogging extends AbstractListener {
  def initLogger() {
    def processId = "${execution.getProcessDefinitionId()} (${execution.getProcessInstanceId()})"
    def logger = LoggerFactory.getLogger(processId)
    
    execution.setVariable("logger", logger)
  }
  
  def testLogger() {
    log "Logger initialized"
  }

  def execute() {
    initLogger()
    testLogger()
  }
}
