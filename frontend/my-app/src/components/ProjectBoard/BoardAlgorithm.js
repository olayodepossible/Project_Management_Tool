import React from "react";

export default function BoardAlgorithm() {
  return <div></div>;
}

export const boardAlgorithm = (errors, project_tasks) => {
  if (project_tasks < 1) {
    if (errors.projectNotFound) {
      return (
        <div className="alert alert-danger text-center" role="alert">
          {errors.projectNotFound}
        </div>
      );
    } else {
      return (
        <div className="alert alert-info text-center" role="alert">
          No Project Task on this board
        </div>
      );
    }
  } else {
    return <Backlog projectTaskProp={project_tasks} />;
  }
};
