import React, { useState } from 'react';

const App1 = () => {
  const [clicks, setClicks] = useState(0);
  return (
    <div>
      <h1>greeting {clicks} times</h1>
      <button onClick={() => setClicks(clicks + 1)}>more</button>
    </div>
  );
}

export default App1;