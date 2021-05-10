import React, {Component} from 'react';
import ProjectTableRow from "./ProjectTableRow";
import {Link} from "react-router-dom";

export default class NewEnvironment extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: this.props.match.params.name,
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this)
    }

    handleSubmit = async e => {
        e.preventDefault();
        console.log(this.state.environmentName)
        const post_response = await fetch(`http://localhost:8080/api/program/add`, { method: 'POST',
            body: JSON.stringify({
                environment: {
                    environmentName: this.state.environmentName,
                    environmentId: this.state.environmentId
                },
                capabilityName: this.state.capabilityName,
                // parentCapabilityId: this.state.parentCapability,
                // paceOfChange: this.state.paceOfChange,
                // targetOperatingModel: this.state.TOM,
                // informationQuality: this.state.informationQuality,
                // applicationFit: this.state.applicationFit,
                // resourceQuality: this.state.resourcesQuality,
                // // status: this.state.expirationDate,
                // level: this.state.level
            }) });
        if (!post_response.ok) {
            console.log('Failed to post capability');
        }
        console.log(`Capability posted with name: ${this.state.capabilityName}`);
    }

    componentDidMount() {
    }

    handleInputChange(event) {
        this.setState({ [event.target.name]: event.target.value })
    }

    projectTableRow() {
        return this.state.environments.map((row, i) => {
            return <ProjectTableRow obj={ row } key={ i }/>
        })
    }

    render() {
        return(
            <div>
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${this.state.environmentName}`}>{this.state.environmentName}</Link></li>
                        <li className="breadcrumb-item active" aria-current="page">Add Program</li>
                    </ol>
                </nav>
            <div className="jumbotron">
                <h3>Add Program</h3>
                <form onSubmit={this.handleSubmit} method="POST">
                <div className="row">
                    <div className="col-sm-6">
                        <div className="form-row">
                            <div className="form-group col-md-6">
                                <label htmlFor="programName">Name Program</label>
                            <input type="text" id="programName" name="programName" className="form-control" placeholder="Name Program"
                                       value={this.state.programName} onChange={this.handleInputChange}/>
                            </div>
                        </div>
                        <div className="form-row">
                            <div className="form-group col-md-6">
                                <label htmlFor="paceOfChange">Add Project</label>
                                <select className="form-control" name="project" id="project" placeholder="Add Project"
                                        value={this.state.project} onChange={this.handleInputChange}>
                                    <option value="1">Project 1</option>
                                    <option value="2">Project 2</option>
                                    <option value="3">Project 3</option>
                                </select>
                            </div>
                        </div>
                        <div className="col-sm-6">
                        <div className="form-row">
                            <div className="form-group col-md-6">
                            <table className=' table table-striped'>
                                <thead>
                                <tr>
                                    <th>Name</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                { this.projectTableRow() }
                                </tbody>
                            </table>
                            </div>
                        </div>
                        </div>
                    </div>
                </div>
                    <button className="btn btn-secondary" type="submit">Submit</button>
                </form>
            </div>
            </div>
        )
    }
}