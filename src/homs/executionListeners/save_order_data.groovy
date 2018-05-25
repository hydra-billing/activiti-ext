package org.activiti.latera.homs.executionListeners

import org.activiti.latera.bss.executionListeners.AbstractListener
import org.activiti.latera.bss.http.HTTPRestProcessor
import org.activiti.engine.delegate.DelegateExecution
import org.activiti.latera.bss.logging.Logging

class SaveOrderData extends AbstractListener {
  
  def saveOrderData(def execution, def logger) {
    def homsUrl = execution.getVariable('homsUrl')
    def homsUser = execution.getVariable('homsUser')
    def homsPassword = execution.getVariable('homsPassword')
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

    def httpProcessor = new HTTPRestProcessor(baseUrl: "$homsUrl/api/")
    httpProcessor.httpClient.auth.basic(homsUser, homsPassword)
    httpProcessor.sendRequest('put', path: "orders/$homsOrderCode", body: homsRequestObj, logger: logger)
  }

  def notify(DelegateExecution execution) {
    def logger = Logging.getLogger(execution)

    Logging.log('/ Saving order data...', "info", logger)
    saveOrderData(execution, logger)
    Logging.log('\\ Order data saved', "info", logger)
  }
}
