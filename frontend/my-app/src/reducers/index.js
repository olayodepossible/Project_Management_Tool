import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import projectReducer from "./projectReducer";
import securityReducer from "./securityReducer";

import BackLogReducer from "./BackLogReducer";

export default combineReducers({
  errors: errorReducer,

  project: projectReducer,

  backlog: BackLogReducer,

  security: securityReducer,
});
