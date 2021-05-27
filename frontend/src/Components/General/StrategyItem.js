import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import "./GeneralTable.css";
import axios from "axios";
import toast from "react-hot-toast";

export default class StrategyItem extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      strategyItems: [],
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
        this.props.history.push("/notfounderror");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/strategyitem/`)
      .then((response) => {
        this.setState({ strategyItems: response.data });
      })
      .catch((error) => {
        toast.error("Could Not Find Strategy Items");
      });
  }

  edit(strategyItemId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/strategyitem/${strategyItemId}`
    );
  }

  fetchDeleteStrategyItems = async (itemId) => {
    await axios
      .delete(`${process.env.REACT_APP_API_URL}/strategyitem/${itemId}`)
      .then((response) => toast.success("Succesfully Deleted Strategy Item"))
      .catch((error) => toast.error("Could not Delete Strategy Item"));
    //REFRESH Strategy Items
    await axios
      .get(`${process.env.REACT_APP_API_URL}/strategyitem/`)
      .then((response) => {
        this.setState({ strategyItems: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Strategy Items");
      });
  };

  delete = async (itemId) => {
    toast(
      (t) => (
        <span>
          <p className="text-center">
            Are you sure you want to remove this strategyItem?
          </p>
          <div className="text-center">
            <button
              className="btn btn-primary btn-sm m-3"
              stlye={{ width: 50, height: 30 }}
              onClick={() => {
                toast.dismiss(t.id);
                this.fetchDeleteStrategyItems(itemId);
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
      <div>
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
            <li className="breadcrumb-item">
              <Link
                to={`/environment/${this.state.environmentName}/strategyItem`}
              >
                Strategy Item
              </Link>
            </li>
          </ol>
        </nav>
        <div className="jumbotron">
          <div>
            <h1 className="display-4" style={{ display: "inline-block" }}>
              Strategy Items
            </h1>
            <Link
              to={`/environment/${this.state.environmentName}/strategyItem/add`}
            >
              <button className="btn btn-primary float-right">
                Add Strategy Item
              </button>
            </Link>
          </div>
          <br />
          <br />
          <MaterialTable
            columns={[
              { title: "ID", field: "itemId" },
              { title: "Name", field: "strategyItemName" },
              { title: "Strategy", field: "strategy.strategyName" },
              { title: "Start", field: "strategy.timeFrameStart" },
              { title: "End", field: "strategy.timeFrameEnd" },
              {
                title: "",
                name: "delete",
                render: (rowData) => (
                  <button className="btn btn-danger">
                    <i
                      onClick={this.delete.bind(this, rowData.itemId)}
                      className="bi bi-trash"
                    ></i>
                  </button>
                ),
              },
              {
                title: "",
                name: "edit",
                render: (rowData) => (
                  <button className="btn btn-secondary">
                    <i
                      onClick={this.edit.bind(this, rowData.itemId)}
                      className="bi bi-pencil"
                    ></i>
                  </button>
                ),
              },
            ]}
            data={this.state.strategyItems}
            options={{
              showTitle: false,
              search: false,
            }}
          />
        </div>
      </div>
    );
  }
}
