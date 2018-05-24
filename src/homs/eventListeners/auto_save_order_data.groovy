package org.activiti.latera.homs.eventListeners

import org.activiti.latera.bss.eventListeners.AbstractListener
import org.activiti.latera.bss.http.HTTPRestProcessor
import org.activiti.engine.delegate.event.ActivitiEvent
import org.activiti.engine.delegate.DelegateExecution


public class AutoSaveOrderData extends AbstractListener {

  def execute(DelegateExecution execution, ActivitiEvent event) {
    if (isSavePossible(execution)) {
      def orderData = getOrderData(execution)

      if (orderData != execution.getVariable('homsOrdDataBuffer')) {
        log('/ Saving order data...', "info", execution)
        saveOrderData(orderData, execution)
        execution.setVariable('homsOrdDataBuffer', orderData)
        log('\\ Order data saved', "info", execution)
      } else {
        log('Order data has not changed, save not needed', "info", execution)
      }
    }
  }

  def private isSavePossible(DelegateExecution execution) {
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

  def private saveOrderData(orderData, execution) {
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
    httpProcessor.sendRequest('put', path: "orders/$homsOrderCode", body: homsRequestObj, execution: execution)
  }

  def public boolean isFailOnException() {
    true
  }
}
