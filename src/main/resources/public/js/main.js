if (!!window.EventSource) {
  const eventSource = new EventSource('products/events');
  const eventList = document.querySelector('ul');

  eventSource.onmessage = e => {
    const newElement = document.createElement('li');
    newElement.textContent = 'Event: ' + e.data;
    eventList.appendChild(newElement);
  }
} else {
  alert("The browser doesn't support SSE");
}
