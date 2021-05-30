import React, { Component } from "react";
import axios from "axios";
import toast from "react-hot-toast";
import { Link, withRouter } from "react-router-dom";

class RecentEnvironmentTableRow extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  async deleteEnvironment(environmentId) {
    await axios
      .delete(`${process.env.REACT_APP_API_URL}/environment/${environmentId}`)
      .then((response) => toast.success("Successfully Removed Environment"))
      .catch((error) => {
        toast.error("Failed to Remove Environment");
      });
    window.location.reload();
  }

  render() {
    return (
      <tr>
        <td>{this.props.obj.environmentId}</td>

        <td>
          <Link to={"/environment/" + this.props.obj.environmentName}>
            {this.props.obj.environmentName}
          </Link>
        </td>
        <td>
          <button className="btn">
            <i
              onClick={() => {
                this.deleteEnvironment(this.props.obj.environmentId);
              }}
              className="bi bi-trash"
            ></i>
          </button>
          <button className="btn">
            <i
              onClick={() => {
                console.log(
                  `/environment/${this.props.obj.environmentId}/edit`
                );
                this.props.history.push(
                  `/environment/${this.props.obj.environmentId}/edit`
                );
              }}
              className="bi bi-pencil"
            ></i>
          </button>
        </td>
      </tr>
    );
  }
}
export default withRouter(RecentEnvironmentTableRow);
