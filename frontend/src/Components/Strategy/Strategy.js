import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import toast from "react-hot-toast";
import API from "../../Services/API";

export default class Strategy extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),

      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      strategies: [],
      showStatusModal: false,
    };
  }

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "status" });
    this.state.api.createEntity({ name: "strategy" });

    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });

    await this.state.api.endpoints.strategy
      .getAll()
      .then((response) => {
        this.setState({ strategies: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Strategies");
      });
  }

  //REDIRECT TO EDIT PAGE
  edit(strategyId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/strategy/${strategyId}`
    );
  }

  //DELETE STRATEGY
  fetchDeleteStrategies = async (strategyId) => {
    await this.state.api.endpoints.strategy
      .delete({ id: strategyId })
      .then((response) => toast.success("Successfully Deleted Strategy"))
      .catch((error) => toast.error("Could not Delete Strategy"));
    //REFRESH STRATEGIES
    await this.state.api.endpoints.strategy
      .getAll()
      .then((response) => {
        this.setState({ strategies: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Strategies");
      });
  };

  //CONFIRM STRAETGY DELETION
  delete = async (strategyId) => {
    toast(
      (t) => (
        <span>
          <p className="text-center">
            Are you sure you want to remove this strategy?
          </p>
          <div className="text-center">
            <button
              className="btn btn-primary btn-sm m-3"
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteStrategies(strategyId);
              }}
            >
              Yes!
            </button>
            <button
              className="btn btn-secondary btn-sm m-3"
              stlye={{ width: 50, height: 30 }}
              onClick={() => toast.dismiss(t.id)}
            >
              No!
            </button>
          </div>
        </span>
      ),
      { duration: 50000 }
    );
  };

  render() {
    return (
      <div className="container">
        <br></br>
        <nav aria-label="breadcrumb">
          <ol className="breadcrumb">
            <li className="breadcrumb-item">
              <Link to={`/`}>Home</Link>
            </li>
            <li className="breadcrumb-item">
              <Link to={`/environment/${this.state.environmentName}`}>
                {this.state.environmentName}
              </Link>
            </li>
            <li className="breadcrumb-item">Strategy</li>
          </ol>
        </nav>
        <MaterialTable
          columns={[
            { title: "ID", field: "strategyId" },
            { title: "Name", field: "strategyName" },
            { title: "Start", field: "timeFrameStart" },
            { title: "End", field: "timeFrameEnd" },
            { title: "Status", field: "status.validityPeriod" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className="btn">
                    <i
                      onClick={this.delete.bind(this, rowData.strategyId)}
                      className="bi bi-trash"
                    ></i>
                  </button>
                  <button className="btn">
                    <i
                      onClick={this.edit.bind(this, rowData.strategyId)}
                      className="bi bi-pencil"
                    ></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.strategies}
          title="Strategies"
          actions={[
            {
              icon: "add",
              tooltip: "Add Strategy",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/strategy/add`
                );
              },
            },
          ]}
        />
      </div>
    );
  }
}
