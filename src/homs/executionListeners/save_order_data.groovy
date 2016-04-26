package org.activiti.latera.homs.executionListeners

import org.activiti.latera.bss.executionListeners.AbstractListener
import org.activiti.engine.delegate.Expression
import org.activiti.latera.bss.http.HTTPRestProcessor

public class SaveOrderData extends AbstractListener {
  private Expression homsUrl
  private Expression homsUser
  private Expression homsPassword
  
  def getParameterValue(def parameterName, def execution) {
    def parameter = this."$parameterName"
    parameter ? (String)parameter.getValue(execution) : execution.getVariable(parameterName)
  }
  
  def saveOrderData(def homsUrl, def homsUser, def homsPassword, def execution) {
    def homsOrderCode = execution.getVariable('homsOrderCode')
    def homsRequestObj = [
      order: [
        data: [:]
      ]
    ]

    for (e in execution.getVariables()) {
      if (!e.key.startsWith('homsOrderData')) continue
      
      def dataKey = e.key.replaceFirst(/^homsOrderData/, "")
      dataKey = (dataKey.getAt(0).toLowerCase() + dataKey.substring(1)).toString()
      homsRequestObj.order.data[dataKey] = e.value
    }

    def httpProcessor = new HTTPRestProcessor(execution: execution, baseUrl: "$homsUrl/api/")
    httpProcessor.httpClient.auth.basic(homsUser, homsPassword)
    httpProcessor.sendRequest('put', path: "orders/$homsOrderCode", body: homsRequestObj)
  }

  def execute() {
    def homsUrl = getParameterValue('homsUrl', execution)
    def homsUser = getParameterValue('homsUser', execution)
    def homsPassword = getParameterValue('homsPassword', execution)

    log '/ Saving order data...'
    saveOrderData(homsUrl, homsUser, homsPassword, execution)
    log '\\ Order data saved'
  }
}
