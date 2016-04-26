package org.activiti.latera.bss.executionListeners

import org.activiti.engine.delegate.*

public class AbstractListener implements ExecutionListener {
  DelegateExecution execution

  def public void notify(DelegateExecution execution) {
    this.execution = execution
    execute()
  }
  
  def protected execute() {
    // do stuff
  }

  def protected log(String msg, String level = "info") {
    def logger = execution.getVariable("logger")

    if (logger) {
      logger."${level}"(msg)
    }
  }
}
