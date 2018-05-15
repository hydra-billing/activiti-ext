package org.activiti.latera.bss.executionListeners

import org.activiti.engine.delegate.*

public class AbstractListener implements ExecutionListener {

  def public void notify(DelegateExecution execution) {
    execute(execution)
  }

  def protected execute(DelegateExecution execution) {
    // do stuff
  }

  def protected log(String msg, String level = "info", DelegateExecution execution = null) {
    def logger = getLogger(execution)

    if (logger) {
      logger."${level}"(msg)
    }
  }

  def protected getLogger(DelegateExecution execution) {
    def logger = null

    if (execution) {
      logger = execution.getVariable("logger")
    }

    logger
  }
}

