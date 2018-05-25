package org.activiti.latera.homs.executionListeners

import org.activiti.latera.bss.executionListeners.AbstractListener
import org.activiti.latera.bss.http.HTTPRestProcessor
import org.activiti.engine.delegate.DelegateExecution
import org.activiti.latera.bss.logging.Logging

class StartOrder extends AbstractListener {
  
  def startOrder(def execution, def logger) {
    def homsUrl = execution.getVariable('homsUrl')
    def homsUser = execution.getVariable('homsUser')
    def homsPassword = execution.getVariable('homsPassword')
    def homsOrderCode = execution.getVariable('homsOrderCode')
    def initiatorEmail = execution.getVariable('initiatorEmail')

    def homsRequestObj = [
      order: [
        state: "in_progress",
        done_at: null,
        bp_id: execution.processInstanceId,
        bp_state: "in_progress",
        user_email: initiatorEmail,
      ]
    ]

    def httpProcessor = new HTTPRestProcessor(baseUrl: "$homsUrl/api/")
    httpProcessor.httpClient.auth.basic(homsUser, homsPassword)
    httpProcessor.sendRequest('put', path: "orders/$homsOrderCode", body: homsRequestObj, logger: logger)
  }

  void notify(DelegateExecution execution) {
    def logger = Logging.getLogger(execution)

    Logging.log('/ Starting order...', "info", logger)
    startOrder(execution, logger)
    Logging.log("\\ Order started", "info", logger)
  }
}
