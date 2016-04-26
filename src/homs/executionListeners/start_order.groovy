package org.activiti.latera.homs.executionListeners

import org.activiti.engine.delegate.Expression
import org.activiti.latera.bss.executionListeners.AbstractListener
import org.activiti.latera.bss.http.HTTPRestProcessor

public class StartOrder extends AbstractListener {
  private Expression homsUrl
  private Expression homsUser
  private Expression homsPassword
  
  def getParameterValue(def parameterName, def execution) {
    def parameter = this."$parameterName"
    parameter ? (String)parameter.getValue(execution) : execution.getVariable(parameterName)
  }
  
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
    httpProcessor.sendRequest('put', path: "orders/$homsOrderCode", body: homsRequestObj)
  }

  def execute() {
    def homsUrl = getParameterValue('homsUrl', execution)
    def homsUser = getParameterValue('homsUser', execution)
    def homsPassword = getParameterValue('homsPassword', execution)

    log '/ Starting order...'    
    startOrder(homsUrl, homsUser, homsPassword, execution)
    log "\\ Order started"
  }
}
