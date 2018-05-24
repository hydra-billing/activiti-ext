package org.activiti.latera.homs.executionListeners

import org.activiti.engine.delegate.Expression
import org.activiti.latera.bss.executionListeners.AbstractListener
import org.activiti.latera.bss.http.HTTPRestProcessor
import org.activiti.engine.delegate.DelegateExecution

public class StartOrder extends AbstractListener {
  
  def startOrder(def homsUrl, def homsUser, def homsPassword, def execution) {
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

    def httpProcessor = new HTTPRestProcessor(execution: execution, baseUrl: "$homsUrl/api/")
    httpProcessor.httpClient.auth.basic(homsUser, homsPassword)
    httpProcessor.sendRequest('put', path: "orders/$homsOrderCode", body: homsRequestObj, execution: execution)
  }

  def execute(DelegateExecution execution) {
    def homsUrl = execution.getVariable('homsUrl')
    def homsUser = execution.getVariable('homsUser')
    def homsPassword = execution.getVariable('homsPassword')

    log('/ Starting order...', "info", execution)
    startOrder(homsUrl, homsUser, homsPassword, execution)
    log("\\ Order started", "info", execution)
  }
}
