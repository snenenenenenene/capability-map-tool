import axios from 'axios';
import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import { Input } from 'reactstrap'
import RecentEnvironmentTableRow from "./RecentEnvironmentTableRow";
import toast from 'react-hot-toast';
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
        await axios.get(`${process.env.REACT_APP_API_URL}/environment/exists-by-environmentname/${this.state.environmentName}`)
        .then(response => {
            if(response.data === true){
                this.props.history.push(`/environment/${this.state.environmentName}`)
                return;
            }
            const formData = new FormData()
            formData.append('environmentName', this.state.environmentName)
            axios.post(`${process.env.REACT_APP_API_URL}/environment/`, formData)
        .then(response => {
            toast.success("Environment Successfully Created!")
            this.props.history.push(`environment/${this.state.environmentName}`);
        }).catch(error => {
            this.props.history.push('/notfounderror')
        })
        })
        .catch( error => {
            toast.error("Failed to Connect to Environments")
        })
    }

    handleInputChange(event) {
        this.setState({ [event.target.name]: event.target.value })
    }

    async componentDidMount() {
        await axios.get(`${process.env.REACT_APP_API_URL}/environment/`)
        .then(response => this.setState({environments: response.data}))
        .catch(error => {
            toast.error("Could not Load Environments")
        })
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
                                <th>ID</th>
                                <th>Name</th>
                                <th></th>
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
                    <button className="btn btn-secondary" type="button" onClick={this.handleSubmit}>Add</button>
                </form>
                </div>
            </div>
        </div>
              </div>
        )
    }
}