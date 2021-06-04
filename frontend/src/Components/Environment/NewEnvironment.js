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
      .then((response) => toast.success("Successfully Deleted Environment"))
      .catch((error) => toast.error("Could not Delete Environment"));
    //REFRESH ENVIRONMENTS
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
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/exists-by-environmentname/${this.state.environmentName}`,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      )
      .then((response) => {
        if (response.data === true) {
          toast.success("This Environment Already Exists!");
          localStorage.setItem(
            "environment",
            JSON.stringify({
              environmentName: this.state.environmentName,
            })
          );
          this.props.history.push(`/environment/${this.state.environmentName}`);
          return;
        }
        const formData = new FormData();
        formData.append("environmentName", this.state.environmentName);
        axios
          .post(`${process.env.REACT_APP_API_URL}/environment/`, formData, {
            headers: {
              Authorization: `Bearer ${jwt}`,
            },
          })
          .then((response) => {
            toast.success("Environment Successfully Created!");
            localStorage.setItem(
              "environment",
              JSON.stringify({
                environmentName: this.state.environmentName,
              })
            );
            this.props.history.push(
              `environment/${this.state.environmentName}`
            );
          })
          .catch((error) => {
            this.props.history.push("/404");
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
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;
    await axios
      .get(`${process.env.REACT_APP_API_URL}/environment/`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
      .then((response) => this.setState({ environments: response.data }))
      .catch((error) => {
        console.error(error);
        toast.error("Could not Load Environments");
      });
  }

  render() {
    return (
      <div className='container'>
        <br></br>
        <nav aria-label='breadcrumb'>
          <ol className='breadcrumb'>
            <li className='breadcrumb-item'>
              <Link to={`/home`}>Home</Link>
            </li>
          </ol>
        </nav>
        <MaterialTable
          actions={[
            {
              icon: "add",
              tooltip: "Add Environment",
              isFreeAction: true,
              onClick: (event) => {
                toast(
                  (t) => (
                    <span>
                      <p className='text-center'>New Environment</p>
                      <form className=' ml-auto' onSubmit={this.handleSubmit}>
                        <input
                          type='text'
                          id='environmentName'
                          name='environmentName'
                          className='form-control'
                          placeholder='Name Environment'
                          onChange={this.handleInputChange}
                          required
                          autoFocus
                        />
                        <div className='text-center'>
                          <button
                            className='btn btn-primary btn-sm m-3'
                            stlye={{ width: 50, height: 30 }}
                            onClick={(e) => {
                              toast.dismiss(t.id);
                              this.handleSubmit(e);
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
                      </form>
                    </span>
                  ),
                  { duration: 50000 }
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "environmentId" },
            { title: "Name", field: "environmentName" },
            {
              title: "",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className='btn btn'>
                    <i
                      className='bi bi-trash'
                      onClick={(e) => {
                        toast(
                          (t) => (
                            <span>
                              <p className='text-center'>
                                Are you sure you want to remove this
                                environment?
                              </p>
                              <div className='text-center'>
                                <button
                                  className='btn btn-primary btn-sm m-3'
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
                        e.stopPropagation();
                      }}
                    ></i>
                  </button>
                  <button
                    className='btn btn'
                    onClick={(e) => {
                      this.props.history.push(
                        `/environment/${rowData.environmentId}/edit`
                      );
                      e.stopPropagation();
                    }}
                  >
                    <i className='bi bi-pencil'></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.environments}
          onRowClick={(event, rowData, togglePanel) => {
            localStorage.setItem(
              "environment",
              JSON.stringify({
                environmentName: rowData.environmentName,
              })
            );
            this.props.history.push(`/environment/${rowData.environmentName}`);
          }}
          title='Dashboard'
        />
      </div>
    );
  }
}
