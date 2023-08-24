import React from 'react';

const App2 = (props) => {
    console.log(props);
    return (
    <div>
      <h1>This is app 2</h1>
      <button onClick={() => alert('hello world')}>Click Me</button>
    </div>
  );
}

export default App2;