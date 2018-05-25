package org.activiti.latera.homs.eventListeners

import org.activiti.latera.bss.eventListeners.AbstractListener
import org.activiti.latera.bss.http.HTTPRestProcessor
import org.activiti.engine.delegate.event.ActivitiEvent
import org.activiti.engine.delegate.DelegateExecution
import org.activiti.latera.bss.logging.Logging

class AutoSaveOrderData extends AbstractListener {

  void onEvent(ActivitiEvent event) {
    def logger = Logging.getLogger(event)
    def execution = getExecution(event)

    if (isSavePossible(execution)) {
      def orderData = getOrderData(execution)

      if (orderData != execution.getVariable('homsOrdDataBuffer')) {
        Logging.log('/ Saving order data...', "info", logger)
        saveOrderData(orderData, execution, logger)
        execution.setVariable('homsOrdDataBuffer', orderData)
        Logging.log('\\ Order data saved', "info", logger)
      } else {
        Logging.log('Order data has not changed, save not needed', "info", logger)
      }
    }
  }

  static private isSavePossible(DelegateExecution execution) {
    execution && execution.getVariable('homsOrderCode') && execution.getVariable('homsOrdDataBuffer')
  }

  def private getOrderData(DelegateExecution execution) {
    def orderData = [:]

    for (e in execution.getVariables()) {
      if (!e.key.startsWith('homsOrderData')) continue

      def dataKey = e.key.replaceFirst(/^homsOrderData/, "")
      dataKey = (dataKey.getAt(0).toLowerCase() + dataKey.substring(1)).toString()
      orderData[dataKey] = e.value
    }

    orderData
  }

  static private saveOrderData(orderData, execution, logger) {
    def homsUrl = execution.getVariable('homsUrl')
    def homsUser = execution.getVariable('homsUser')
    def homsPassword = execution.getVariable('homsPassword')
    def homsOrderCode = execution.getVariable('homsOrderCode')

    def homsRequestObj = [
        order: [
            data: orderData
        ]
    ]

    def httpProcessor = new HTTPRestProcessor(baseUrl: "$homsUrl/api/")
    httpProcessor.httpClient.auth.basic(homsUser, homsPassword)
    httpProcessor.sendRequest('put', path: "orders/$homsOrderCode", body: homsRequestObj, logger: logger)
  }
}
