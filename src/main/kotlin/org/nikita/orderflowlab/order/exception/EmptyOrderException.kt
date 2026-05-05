package org.nikita.orderflowlab.order.exception

class EmptyOrderException :
    RuntimeException("Order must contain at least one line")