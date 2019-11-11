import EventBus from "vertx3-eventbus-client";
const eventBus = new EventBus(window.location.href+"eventbus");
console.log('NEW SOCKET CREATED ::::::::::::::::::::::::::::::::');

export default eventBus
