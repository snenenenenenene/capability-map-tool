import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import { Input } from 'reactstrap'
import RecentEnvironmentTableRow from "./RecentEnvironmentTableRow";


export default class NewEnvironment extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environmentName: '',
            environments: []
            };
        this.handleInputChange = this.handleInputChange.bind(this)
        this.handleSubmit = this.handleSubmit.bind(this);
    }

     handleSubmit = async e => {
        e.preventDefault();
        console.log("submit")
         console.log(this.state.environmentName)
        const post_response = await fetch(`http://localhost:8080/environment/add`,
            {
            method: 'POST',
                headers: {
                    'Accept': 'multipart/form-data',
                    'Content-Type': 'multipart/form-data'
                },
            body: JSON.stringify({environmentName: 'this.state.environmentName'}) });
        console.log(post_response)
        if (!post_response.ok) {
            console.log('Failed to upload via presigned POST');
        }
        console.log(`File uploaded via presigned POST with key: ${this.state.environmentName}`);
        this.props.history.push(`environment/${this.state.environmentName}`);
    }


    handleInputChange(event) {
        this.setState({ [event.target.name]: event.target.value })
    }

    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/environment/all`);
        const data = await response.json();
        this.setState({environments: data});
        console.log(this.state.environments);
    }

    recentEnvironmentTableRow() {
        return this.state.environments.map((row, i) => {
            return <RecentEnvironmentTableRow obj={ row } key={ i }/>
        })
    }

    render() {
          return(
              <div>
              <nav aria-label="breadcrumb">
                  <ol className="breadcrumb">
                      <li className="breadcrumb-item"><Link to={`/home`}>Home</Link></li>
                      <li className="breadcrumb-item"><Link to={`/add`}>Add Environment</Link></li>
                  </ol>
              </nav>
            <div className="jumbotron">
            <h1>Add Environment</h1>
            <div className="row">
                <div className="col-sm-6">
                    <div>
                        <p>Recent Environments</p>
                        <table className=' table table-striped'>
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                            </tr>
                            </thead>
                            <tbody>
                            { this.recentEnvironmentTableRow() }
                            </tbody>
                        </table>
                    </div>
                </div>
                <div className="col-sm-6">
                <p>New Environments</p>
                <form className="form-inline" onSubmit={this.handleSubmit}>
                    <Input type="text" name="environmentName" value={this.state.environmentName} onChange={this.handleInputChange} className="form-control" placeholder="New Environment"/>
                    <button className="btn primary" type="button" onClick={this.handleSubmit}>Add</button>
                </form>
                </div>
            </div>
        </div>
              </div>
        )
    }
}