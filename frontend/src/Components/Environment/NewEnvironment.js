import axios from "axios";
import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import MaterialTable from "material-table";

export default class NewEnvironment extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environmentName: "",
      environments: [],
      showModal: false,
    };
    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  fetchDeleteEnvironments = async (environmentId) => {
    await axios
      .delete(`${process.env.REACT_APP_API_URL}/environment/${environmentId}`)
      .then((response) => toast.success("Succesfully Deleted Environment"))
      .catch((error) => toast.error("Could not Delete Environment"));
    //REFRESH CAPABILITIES
    await axios
      .get(`${process.env.REACT_APP_API_URL}/environment/`)
      .then((response) => {
        this.setState({ environments: response.data });
      })
      .catch((error) => {
        toast.error("Could not Find Environment");
      });
  };

  handleSubmit = async (e) => {
    e.preventDefault();
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/exists-by-environmentname/${this.state.environmentName}`
      )
      .then((response) => {
        if (response.data === true) {
          toast.success("This Environment Already Exists!");
          this.props.history.push(`/environment/${this.state.environmentName}`);
          return;
        }
        const formData = new FormData();
        formData.append("environmentName", this.state.environmentName);
        axios
          .post(`${process.env.REACT_APP_API_URL}/environment/`, formData)
          .then((response) => {
            toast.success("Environment Successfully Created!");
            this.props.history.push(
              `environment/${this.state.environmentName}`
            );
          })
          .catch((error) => {
            this.props.history.push("/notfounderror");
          });
      })
      .catch((error) => {
        toast.error("Failed to Connect to Environments");
      });
  };

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  async componentDidMount() {
    await axios
      .get(`${process.env.REACT_APP_API_URL}/environment/`)
      .then((response) => this.setState({ environments: response.data }))
      .catch((error) => {
        toast.error("Could not Load Environments");
      });
  }

  render() {
    return (
      <div>
        <br></br>
        <nav aria-label="breadcrumb">
          <ol className="breadcrumb">
            <li className="breadcrumb-item">
              <Link to={`/home`}>Home</Link>
            </li>
          </ol>
        </nav>
        <div>
          <div className="jumbotron shadow">
            <div className="form-inline">
              <h1 className="display-4">Dashboard</h1>
              <form
                className="input-group ml-auto"
                onSubmit={this.handleSubmit}
              >
                <button
                  className="btn btn-danger"
                  type="button"
                  onClick={this.handleSubmit}
                >
                  Add
                </button>
                <input
                  type="text"
                  id="environmentName"
                  name="environmentName"
                  className="form-control"
                  placeholder="Name Environment"
                  value={this.state.environmentName}
                  onChange={this.handleInputChange}
                  required
                />
              </form>
            </div>

            <hr></hr>
            <div className="row">
              <div className="col-sm">
                <div>
                  <MaterialTable
                    columns={[
                      { title: "ID", field: "environmentId" },
                      { title: "Name", field: "environmentName" },
                      {
                        title: "",
                        name: "actions",
                        render: (rowData) => (
                          <>
                            <button className="btn btn">
                              <i
                                className="bi bi-trash"
                                onClick={(e) => {
                                  toast(
                                    (t) => (
                                      <span>
                                        <p className="text-center">
                                          Are you sure you want to remove this
                                          environment?
                                        </p>
                                        <div className="text-center">
                                          <button
                                            className="btn btn-primary btn-sm m-3"
                                            stlye={{ width: 50, height: 30 }}
                                            onClick={() => {
                                              toast.dismiss(t.id);
                                              this.fetchDeleteEnvironments(
                                                rowData.environmentId
                                              );
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
                                  e.stopPropagation();
                                }}
                              ></i>
                            </button>
                            <button
                              className="btn btn"
                              onClick={(e) => {
                                this.props.history.push(
                                  `/environment/${rowData.environmentId}/edit`
                                );
                                e.stopPropagation();
                              }}
                            >
                              <i className="bi bi-pencil"></i>
                            </button>
                          </>
                        ),
                      },
                    ]}
                    data={this.state.environments}
                    onRowClick={(event, rowData, togglePanel) =>
                      this.props.history.push(
                        `/environment/${rowData.environmentName}`
                      )
                    }
                    title="Environments"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
