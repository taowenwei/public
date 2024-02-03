import * as React from "react";

const FormikContext = React.createContext();

const initState = {
  firstName: "",
  lastName: "",
  state: "",
  email: "",
};

const FormikReducer = (state, action) => {
  let newState = { ...state };
  switch (action.type) {
    default:
      throw new Error(`Unknown action type: ${action.type}`);
  }
  console.log(newState);
  return newState;
};

export const FormikProvider = ({ children }) => {
  const [state, dispatch] = React.useReducer(FormikReducer, initState);
  const snapshot = React.useMemo(
    () => ({ state, dispatch }),
    [state, dispatch]
  );
  return (
    <FormikContext.Provider value={snapshot}>{children}</FormikContext.Provider>
  );
};

export const useFormikContext = () => {
  const context = React.useContext(FormikContext);
  if (context === undefined) {
    throw new Error("useFormikContext has to be used with FormikContext");
  }
  return context;
};
