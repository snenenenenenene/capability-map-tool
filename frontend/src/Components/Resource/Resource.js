import React, { Component } from "react";
import { Link } from "react-router-dom";
import MaterialTable from "material-table";
import axios from "axios";

export default class Resource extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      resources: [],
      reload: false,
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
        console.log(error);
        this.props.history.push("/404");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/resource/`)
      .then((response) => {
        this.setState({ resources: response.data });
      })
      .catch((error) => {
        console.log(error);
        // this.props.history.push('/error')
      });
  }

  edit(resourceId) {
    this.props.history.push(
      `/environment/${this.state.environmentName}/resource/${resourceId}`
    );
  }
  //DELETE resource AND REMOVE ALL CHILD resources FROM STATE
  delete = async (resourceId) => {
    if (window.confirm("Are you sure you want to delete this resource?")) {
      await axios
        .delete(`${process.env.REACT_APP_API_URL}/resource/${resourceId}`)
        .catch((error) => console.error(error));
      //REFRESH resources
      await axios
        .get(`${process.env.REACT_APP_API_URL}/resource/`)
        .then((response) => {
          this.setState({ resources: [] });
          this.setState({ resources: response.data });
        })
        .catch((error) => {
          console.log(error);
          this.props.history.push("/error");
        });
    }
  };

  render() {
    return (
      <div className='container'>
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
            <li className='breadcrumb-item'>Resources</li>
          </ol>
        </nav>
        <MaterialTable
          title='Resources'
          actions={[
            {
              icon: "add",
              tooltip: "Add Resource",
              isFreeAction: true,
              onClick: (event) => {
                this.props.history.push(
                  `/environment/${this.state.environmentName}/resource/add`
                );
              },
            },
          ]}
          columns={[
            { title: "ID", field: "resourceId" },
            { title: "Name", field: "resourceName" },
            {
              title: "Actions",
              name: "actions",
              render: (rowData) => (
                <div>
                  <button className='btn'>
                    <i
                      onClick={this.delete.bind(this, rowData.resourceId)}
                      className='bi bi-trash'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={this.edit.bind(this, rowData.resourceId)}
                      className='bi bi-pencil'
                    ></i>
                  </button>
                  <button className='btn'>
                    <i
                      onClick={() => this.handleItemModal()}
                      className='bi bi-app-indicator'
                    ></i>
                  </button>
                </div>
              ),
            },
          ]}
          data={this.state.resources}
        />
      </div>
    );
  }
}
