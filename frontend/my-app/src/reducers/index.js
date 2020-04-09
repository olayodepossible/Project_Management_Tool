import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import projectReducer from "./projectReducer";

import BackLogReducer from "./BackLogReducer";
/*
import securityReducer from "./securityReducer";  */

export default combineReducers({
  errors: errorReducer,

  project: projectReducer,

  backlog: BackLogReducer,
  /*
  security: securityReducer, */
});
