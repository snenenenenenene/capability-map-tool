import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import axios from "axios";
import toast from "react-hot-toast";

export default class Strategy extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      strategies: [],
      showStatusModal: false,
    };
  }

  async componentDidMount() {
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`
      )
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/strategy/`)
      .then((response) => {
        this.setState({ strategies: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Strategies");
      });
  }

  edit(strategyId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/strategy/${strategyId}`
    );
  }

  fetchDeleteStrategies = async (strategyId) => {
    await axios
      .delete(`${process.env.REACT_APP_API_URL}/strategy/${strategyId}`)
      .then((response) => toast.success("Successfully Deleted Strategy"))
      .catch((error) => toast.error("Could not Delete Strategy"));
    //REFRESH Strategies
    await axios
      .get(`${process.env.REACT_APP_API_URL}/strategy/`)
      .then((response) => {
        this.setState({ strategies: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Strategies");
      });
  };

  delete = async (strategyId) => {
    toast(
      (t) => (
        <span>
          <p className='text-center'>
            Are you sure you want to remove this strategy?
          </p>
          <div className='text-center'>
            <button
              className='btn btn-primary btn-sm m-3'
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteStrategies(strategyId);
              }}
            >
              Yes!
            </button>
            <button
              className='btn btn-secondary btn-sm m-3'
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
      <div>
        <br></br>
        <nav aria-label='breadcrumb'>
          <ol className='breadcrumb'>
            <li className='breadcrumb-item'>
              <Link to={`/`}>Home</Link>
            </li>
            <li className='breadcrumb-item'>
              <Link to={`/environment/${this.state.environmentName}`}>
                {this.state.environmentName}
              </Link>
            </li>
            <li className='breadcrumb-item'>
              <Link to={`/environment/${this.state.environmentName}/strategy`}>
                Strategy
              </Link>
            </li>
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
                  <button className='btn'>
                    <i
                      onClick={this.delete.bind(this, rowData.strategyId)}
                      className='bi bi-trash'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={this.edit.bind(this, rowData.strategyId)}
                      className='bi bi-pencil'
                    ></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.strategies}
          title='Strategies'
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
