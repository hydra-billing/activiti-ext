package org.activiti.latera.bss.executionListeners

import org.activiti.engine.delegate.DelegateExecution
import org.activiti.latera.bss.logging.Logging
import org.slf4j.Logger

public class InitLogging extends AbstractListener {

  def testLogger(Logger logger) {
    Logging.log("Logger initialized", "info", logger)
  }

  def notify(DelegateExecution execution) {
    testLogger(Logging.getLogger(execution))
  }
}
